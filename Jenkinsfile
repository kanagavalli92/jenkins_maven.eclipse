pipeline{
agent any
tools {
maven 'Maven 3.9.10'   // must match Jenkins tool name
jdk 'Java JDK 17'      // must match Jenkins tool name
}
stages{
stage("clean"){
steps{
echo "Start Clean"
bat "mvn clean"
}
}
stage("test"){
steps{
echo "Start Test"
bat "mvn test"
}
}
stage("build"){
steps{
echo "Start build"
bat "mvn install -DskipTests"
}
}
stage("scan") {
            steps {
                echo "Start Scan"
                bat "mvn sonar:sonar"
}
}                
}
}