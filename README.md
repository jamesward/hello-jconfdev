# Hello, jconf.dev

Run Locally:
```
./mvnw compile exec:exec
```

Visit: [http://localhost:8080](http://localhost:8080)

Build & Run a Container:
```
./mvnw compile jib:dockerBuild -Dimage=hello-jconfdev
```

Deploy with a few clicks:

[![Run on Google Cloud](https://deploy.cloud.run/button.svg)](https://deploy.cloud.run)



## Cluster Setup

```
gcloud beta container clusters create jconfdev \
  --addons=HorizontalPodAutoscaling,HttpLoadBalancing,Istio \
  --istio-config=auth=MTLS_PERMISSIVE \
  --release-channel=regular \
  --machine-type=e2-medium \
  --enable-stackdriver-kubernetes --enable-ip-alias \
  --enable-autoscaling --num-nodes=10 \
  --enable-autorepair \
  --scopes=cloud-platform

kubectl create clusterrolebinding cluster-admin-binding \
  --clusterrole=cluster-admin \
  --user=$(gcloud config get-value core/account)

# Tekton

kubectl apply \
  -f https://storage.googleapis.com/tekton-releases/pipeline/previous/v0.16.3/release.yaml \
  -f https://storage.googleapis.com/tekton-releases/triggers/previous/v0.8.1/release.yaml

kubectl wait --for=condition=ready -n tekton-pipelines pod --all

kubectl apply \
  -f https://github.com/tektoncd/catalog/raw/master/task/git-clone/0.2/git-clone.yaml \
  -f https://github.com/tektoncd/catalog/raw/master/task/jib-maven/0.1/jib-maven.yaml \
  -f https://github.com/tektoncd/catalog/raw/master/task/kn/0.1/kn.yaml \
  -f https://github.com/tektoncd/catalog/raw/master/task/kn/0.1/kn-deployer.yaml

# Knative

kubectl apply \
  -f https://github.com/knative/serving/releases/download/v0.17.2/serving-crds.yaml \
  -f https://github.com/knative/serving/releases/download/v0.17.2/serving-core.yaml \
  -f https://github.com/knative/net-istio/releases/download/v0.17.1/release.yaml \
  -f https://github.com/knative/serving/releases/download/v0.17.2/serving-default-domain.yaml
```

## CI/CD

Create the pipeline:
```
kubectl apply -f pipeline.yaml
```

Run the pipeline manually:
```
kubectl apply -f pipelinerun.yaml
```

Check the run status:
```
kubectl describe pipelinerun/build-deploy-pipeline-run
```

Check the logs:
```
kubectl logs -l tekton.dev/pipelineTask=test
kubectl logs -l tekton.dev/pipelineTask=source-to-image -c step-build-and-push
kubectl logs -l tekton.dev/pipelineTask=deploy
```
