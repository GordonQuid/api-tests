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
                        results: [[path: "/etc/jenkins/jenkins_home/workspace/root/jenkins/workspace/api-tests-run/allure-results"]],
                        disable: false,
                        reportBuildPolicy: "ALWAYS"
                )
            }
        } finally {
            deleteDir()
        }
    }
}