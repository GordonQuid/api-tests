node("image-builder") {
    timestamps {
        wrap([$class: "BuildUser"]) {
            currentBuild.description = "USER: ${env.BUILD_USER}\nBRANCH: ${params.BRANCH}"
        }
        try {
            stage("Checkout") {
                checkout scm
            }
            stage("Build Docker image") {
                docker.withRegistry("http://localhost:5005") {
                    docker.build("${params.BRANCH}:latest").push()
                }
            }
        } finally {
            cleanWs(deleteDirs: true)
        }
    }
}