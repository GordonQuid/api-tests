node("image-builder") {
    timestamps {
        wrap([$class: "BuildUser"]) {
            currentBuild.description = "USER: ${env.BUILD_USER}\nBRANCH: ${env.BRANCH}"
        }
        try {
            stage("Checkout") {
                checkout scm
            }
            stage("Build Docker image") {
                docker.withRegistry("http://localhost:5005") {
                    docker.build("api-tests:${env.BRANCH}").push()
                }
            }
        } finally {
            cleanWs(deleteDirs: true)
        }
    }
}