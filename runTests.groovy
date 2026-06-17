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
        } finally {
            deleteDir()
        }
    }
}