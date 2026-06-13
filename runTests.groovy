node("runner") {
    timestamps {
        wrap([$class: "BuildUser"]) {
            currentBuild.description = "USER: ${env.BUILD_USER}\nBRANCH: ${env.BRANCH}"
        }
        try {
            stage("Checkout") {
                checkout scm
            }
            stage("Running tests") {
                ansiblePlaybook playbook: "playbook.yml",
                        extraVars: [
                                branch: "${env.BRANCH}"
                        ]
            }
        } finally {
            deleteDir()
        }
    }
}