pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolution {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "TranzoWallet"

include(":app")
include(":core:crypto")
include(":core:keystore")
include(":core:security")
include(":data:network")
include(":data:repository")
include(":data:localdb")
include(":domain:model")
include(":domain:usecase")
include(":feature:onboarding")
include(":feature:wallet")
include(":feature:tokens")
include(":feature:nft")
include(":feature:settings")
include(":feature:payments")
include(":ui:frost-components")
include(":ui:theme")
