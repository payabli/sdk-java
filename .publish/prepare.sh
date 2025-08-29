# Write key ring file
echo "$SDK_MAVEN_GPG_SECRET_KEY" > armored_key.asc
gpg -o publish_key.gpg --dearmor armored_key.asc

# Generate gradle.properties file
echo "signing.keyId=$SDK_MAVEN_GPG_SECRET_KEY_ID" > gradle.properties
echo "signing.secretKeyRingFile=publish_key.gpg" >> gradle.properties
echo "signing.password=$SDK_MAVEN_GPG_PASSWORD" >> gradle.properties
