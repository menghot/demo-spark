# --conf spark.scheduler.pool=high_priority
local:
	/Users/simon/tools/spark-3.5.1-bin-hadoop3/bin/spark-submit \
		--conf spark.scheduler.pool=production \
		--master spark://192.168.80.241:7077 \
		--class org.example.SparkApp --deploy-mode client \
		/Users/simon/workspaces/deom-spark-lineage/target/deom-spark-lineage-1.0-SNAPSHOT.jar

clean:
	kubectl delete pods $(kubectl get pods | grep -v RESTARTS | awk '{print $1}')

submit-k8s:
	/Users/simon/tools/spark-3.5.1-bin-hadoop3/bin/spark-submit \
		--conf spark.scheduler.pool=production \
		--master k8s://https://127.0.0.1:6443 \
		--class org.example.SparkApp --deploy-mode client \
		--name sparkapp \
		--class org.example.SparkApp \
		--jars /Users/simon/workspaces/deom-spark-lineage/iceberg-spark-runtime-3.5_2.12-1.5.2.jar \
		--conf spark.executor.instances=1 \
		--conf spark.kubernetes.container.image=spark:3.5.1-scala2.12-java17-ubuntu \
		--conf spark.kubernetes.namespace=default \
		--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark \
		--conf spark.kubernetes.driver.volumes.hostPath.libs.mount.path=/Users/simon/workspaces/deom-spark-lineage/target \
		--conf spark.kubernetes.driver.volumes.hostPath.libs.options.path=/Users/simon/workspaces/deom-spark-lineage/target \
		--conf spark.kubernetes.executor.volumes.hostPath.libs.mount.path=/Users/simon/workspaces/deom-spark-lineage/target \
		--conf spark.kubernetes.executor.volumes.hostPath.libs.options.path=/Users/simon/workspaces/deom-spark-lineage/target \
		--conf spark.kubernetes.file.upload.path=/Users/simon/workspaces/deom-spark-lineage/target \
		local:///Users/simon/workspaces/deom-spark-lineage/target/deom-spark-lineage-1.0-SNAPSHOT.jar

submit-standalone:
	/Users/simon/tools/spark-3.5.1-bin-hadoop3/bin/spark-submit \
	--conf spark.standalone.submit.waitAppCompletion=false  \
	--deploy-mode cluster \
	--master spark://192.168.80.241:7077 \
	--class org.example.HiveApp \
	/Users/simon/workspaces/deom-spark-lineage/target/deom-spark-lineage-1.0-SNAPSHOT.jar

submit-standalone-wait:
	/Users/simon/tools/spark-3.5.1-bin-hadoop3/bin/spark-submit \
	--conf spark.standalone.submit.waitAppCompletion=true  \
	--deploy-mode client \
	--master spark://192.168.80.241:7077 \
	--class org.example.HiveApp /Users/simon/workspaces/deom-spark-lineage/target/deom-spark-lineage-1.0-SNAPSHOT.jar

