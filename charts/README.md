# Helm chart for the Telescreen app

Telescreen services are stateless. Please provide persistence for the other services and databases on your own.

Run Minikube and install Helm or use existing cluster.

``` sh
minikube start  --cpus 6 --memory 16786 \
   --disk-size 50GB --vm-driver virtualbox \ --kubernetes-version='v1.15.0'
minikube addons enable ingress
helm init
```

## Preparing

**Create `telescreen` namespace**

```sh
kubectl create -f - <<EOF
kind: Namespace
apiVersion: v1
metadata:
  name: telescreen
EOF
```

**Clone [Helm Charts](https://github.com/helm/charts) repository**

```sh
git clone https://github.com/helm/charts.git
cd charts
```

**Install [MongoDB](https://github.com/helm/charts/tree/master/stable/mongodb) or [MongoDB ReplicaSet](https://github.com/helm/charts/tree/master/stable/mongodb-replicaset)** with required options in the `telescreen` namespace. Or create a service that points to an existing database outside the namespace or cluster.

```sh
helm upgrade --install mongodb stable/mongodb \
  --namespace telescreen \
  --set mongodbUsername=user \
  --set mongodbPassword=passpass \
  --set mongodbDatabase=telescreen \
  --set persistence.enabled=false
```

**Install [Minio](https://github.com/helm/charts/tree/master/stable/minio)** with required options in the `telescreen` namespace. Or create a service that points to an existing app outside the namespace or cluster.

```sh
helm upgrade --install minio stable/minio \
  --namespace telescreen \
  --set accessKey=user,secretKey=passpass,replicas=1 \
  --set defaultBucket.enabled=true,defaultBucket.name=app \
  --set ingress.enabled=true,ingress.hosts[0]=minio.192.168.99.103.xip.io \
  --set persistence.enabled=false
```

**Install [Kafka](https://github.com/helm/charts/tree/master/incubator/kafka)** with required options in the `telescreen` namespace. Or create a service that points to an existing cluster outside the namespace.

Set the following `topics` section inside the `incubator/kafka/values.yaml`:

```yaml
topics: []
  - name: wellbeing
    partitions: 1
    replicationFactor: 1
  - name: video
    partitions: 1
    replicationFactor: 1
  - name: music
    partitions: 1
    replicationFactor: 1
  - name: timelog
    partitions: 1
    replicationFactor: 1
  - name: mifit
    partitions: 1
    replicationFactor: 1
  - name: message
    partitions: 1
    replicationFactor: 1
  - name: call
    partitions: 1
    replicationFactor: 1
  - name: media
    partitions: 1
    replicationFactor: 1
  - name: autotimer
    partitions: 1
    replicationFactor: 1
```

And install chart

```sh
helm repo add common https://kubernetes-charts-incubator.storage.googleapis.com/
helm dependency update incubator/kafka
helm upgrade --install kafka -f incubator/kafka/values.yaml incubator/kafka \
  --namespace telescreen \
  --set replicas=1,persistence.enabled=false \
  --set configurationOverrides.auto.create.topics.enable=true
```

## Installing Telescreen

Clone repository and specify variables in the `charts/telescreen/values.yaml` file.

```sh
git clone https://github.com/panfio/telescreen.git
cd telescreen
helm upgrade --install telescreen charts/telescreen \
  --set gatewayHost=telescreen.192.168.99.103.xip.io \
  --namespace telescreen
```
