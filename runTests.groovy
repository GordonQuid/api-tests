node("runner") {
    timestamps {
        wrap([$class: "BuildUser"]) {
            currentBuild.description = "USER: ${env.BUILD_USER}\nBRANCH: ${params.BRANCH}"
        }
        try {
            stage("Checkout") {
                checkout scm
            }
            stage("Running tests") {
                sh "pwd"
                sh "echo WORKSPACE=${env.WORKSPACE}"
                ansiblePlaybook playbook: "playbook.yml",
                        extraVars: [
                                branch: "${params.BRANCH}"
                        ]
            }
            stage("Allure report") {
                allure(
                        results: [[path: "allure-results"]],
                        disable: false,
                        reportBuildPolicy: "ALWAYS"
                )
            }
            stage("Send notifications") {
                def summary = junit testResults: "**/surefire-reports/*.xml"
                String message = """Test Summary
                                    |JOB: ${env.JOB_NAME}
                                    |${currentBuild.description}
                                    |
                                    |Total: ${summary.totalCount}
                                    |Passed: ${summary.passCount}
                                    |Failed: ${summary.failCount}
                                    |Skipped: ${summary.skipCount}
                                    |
                                    |See [full report](${env.BUILD_URL}allure) for details."""
                        .stripMargin()
                withCredentials([
                        string(credentialsId: "mattermost-webhook", variable: "WEBHOOK"),
                        string(credentialsId: "mattermost-users", variable: "USERS")
                ]) {
                    env.USERS.tokenize(",").each { username ->
                        httpRequest consoleLogResponseBody: true,
                                contentType: "APPLICATION_JSON",
                                httpMode: "POST",
                                requestBody: "{\"text\":\"${message}\",\"channel\":\"@${username}\",\"username\":\"Jenkins\"}",
                                url: "${env.WEBHOOK}"
                    }
                }
            }
        } finally {
            deleteDir()
        }
    }
}