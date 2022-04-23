#/bin/sh

JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11-latest/Contents/Home
LOCAL_API_HOSTPORT=apiserver:8089
GAE_PARTITION=dev
/Users/ludo/.sdkman/candidates/java/current/bin/java \
 --add-opens java.base/java.lang=ALL-UNNAMED  \
 --add-opens java.base/java.nio.charset=ALL-UNNAMED \
 -showversion -XX:+PrintCommandLineFlags \
 -Djava.class.path=/Users/ludo/Downloads/GAE/runtime-main.jar \
  -Dclasspath.runtimebase=/Users/ludo/Downloads/GAE: \
  com/google/apphosting/runtime/JavaRuntimeMainWithDefaults \
 --fixed_application_path=/Users/ludo/a/appengine-guestbook-java/target/appengine-staging  \
  /Users/ludo/Downloads/GAE


