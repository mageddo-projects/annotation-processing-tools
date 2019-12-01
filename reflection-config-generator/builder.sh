#!/bin/bash

set -e

validateRelease(){
	APP_VERSION=$1
	if git rev-parse "$APP_VERSION^{}" >/dev/null 2>&1; then
		echo "> Version already exists $APP_VERSION"
		exit 0
	fi
}

case $1 in

	deploy )

		APP_VERSION=$(./gradlew -q version)
		echo "> deploying ${APP_VERSION}"

		validateRelease $APP_VERSION

		# preparing gpg key
		mkdir -p $HOME/.gnupg/
		openssl aes-256-cbc -d -pbkdf2 -k ${GPG_CIPHER_KEY} -in files/deployment/.gnupg/secring.gpg.encrypted -out $HOME/.gnupg/secring.gpg

		# building and deploying to sonatype nexys
		export GRADLE_PROJECT_OPTS="-PossrhUsername=${NEXUS_USERNAME} -PossrhPassword=${NEXUS_PASSWORD}"
		export GRADLE_PROJECT_OPTS="${GRADLE_PROJECT_OPTS} -Psigning.password=${GPG_PASSWORD} -Psigning.keyId=${GPG_KEY_ID}"
		export GRADLE_PROJECT_OPTS="${GRADLE_PROJECT_OPTS} -Psigning.secretKeyRingFile=$HOME/.gnupg/secring.gpg"
		export GRADLE_PROJECT_OPTS="${GRADLE_PROJECT_OPTS} -PrepositoryUrl=$REPOSITORY_URL"

		./gradlew build publishToSonatype closeAndReleaseRepository ${GRADLE_PROJECT_OPTS}

		# publishing tag
		REMOTE="https://${REPO_TOKEN}@$(echo $REPOSITORY_URL | awk -F '//' '{print $2}')"
		git tag ${APP_VERSION}
		git push "$REMOTE" --tags
		git status
		echo "> Branch pushed. branch=${CURRENT_BRANCH}, version=${APP_VERSION}"

	;;

esac
