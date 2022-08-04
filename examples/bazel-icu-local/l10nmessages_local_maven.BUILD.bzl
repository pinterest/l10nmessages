java_import(
    name = "l10nmessages",
    jars = [
       "l10nmessages/target/l10nmessages-0.0.9-SNAPSHOT.jar",
    ],
    visibility = ["//visibility:public"],
)

java_import(
    name = "l10nmessages-proc",
    jars = [
       "l10nmessages-proc/target/l10nmessages-proc-0.0.9-SNAPSHOT.jar",
    ],
    visibility = ["//visibility:public"],
)
