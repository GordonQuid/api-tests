node("image-builder") {
    timestamps {
        def dockerRegistry = "http://localhost:5005"

        def branch = params.BRANCH
        def imageName = params.IMAGE_NAME
        def version = params.VERSION

        wrap([$class: "BuildUser"]) {
            currentBuild.description = """
USER: ${env.BUILD_USER}
BRANCH: ${branch}
IMAGE_NAME: ${imageName}
VERSION: ${version}
"""
        }

        try {
            stage("Checkout") {
                checkout scm
            }

            stage("Build Docker image") {
                docker.withRegistry(dockerRegistry) {
                    docker.build("${imageName}:${version}").push()
                }
            }
        } finally {
            cleanWs(deleteDirs: true)
        }
    }
}