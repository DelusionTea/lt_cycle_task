
export PATH="$PATH:/home/pprb_test/.local/bin"
export API_HOST='https://api.lt-terra000028-eds.ocp.delta.sbrf.ru:6443'
export TOKEN='eyJhbGciOiJSUzI1NiIsImtpZCI6IlVxWC1fMm1vZUNqaWtkbEJVVF9hb1hHV2phSGE1MXFTUDFlb3YxY1ZJMVUifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJjaTAyNzI2MzY2LWNvcnAtc2JlcnJhdGluZy1yaXNrYmxva2luZy1tb2JpbGUiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlY3JldC5uYW1lIjoiamVua2lucy10b2tlbiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJqZW5raW5zIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQudWlkIjoiMjY5ZGE3ZWUtMDdjOC00ZWU4LTlkZDMtNDVhOWMxMzhmMWNiIiwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50OmNpMDI3MjYzNjYtY29ycC1zYmVycmF0aW5nLXJpc2tibG9raW5nLW1vYmlsZTpqZW5raW5zIn0.Nj-WGdjD7xXC67vc8Hk2PPHGzIpB-LlVEM6OIpFS_AZdOPKkXi68Q5np7Kvzj8zBQWy2owtZwkgjzcMz1uICBkOmhtUF9lCOzQH-Ref4T67p6hLH5SFL5pCcunzDDJ88Ce4kFgQTPu8EyW7aGdiVrZpfep2r_LUL0gykNcKUpmRNzU0De6tZkfiKgHahCTI223FNvtBUkLJAnE06uGMGSn30tFFQWLIPhkktESpEbYFhhTwLpbBpGd60ckOBbNBr7UDp2T0JqBo9yxiKittZDHt8UsBWeI7hG6Ct53kOV-GAJPNmivqKIR_KklDmL_mMMeeucjZIKXWJBKeMeJepWbozxFf2iBD_5kuVa3IwmWsOde5fTRFuNcB0y-vo0W9GolJIQCOzsBCspDHvucnxjB3suQTMLK76eiSTbYeEP2LiEtPb5MPHFQHBmLorM-g1p6IXMNvqrLk9WvLh4ZHiMGLByhAIYgLKICC7i9VI4F4XW5fb40H54G0bm1Jy-wjU_q7z0btHSrkAsSDvi8uGdEfcQqNr4JZYj4S0GdVqzB3hI3c9FMio14Q6QgbPCQLt1mvp4_1N0fDQepEkcsOAVbourasgGbl8451qc03IYwObiODZgGtvhTxt1XH5L-lHmjTdaOpPvG5ZyiEP0xsDKWc8QjY55vdmlIEI5D7J4vc'
export NAMESPACE='ci02726366-corp-sberrating-riskbloking-mobile'

kubectl version --client


kubectl config set-credentials riski-user --token=$TOKEN
kubectl config set-cluster riski-cluster --server=$API_HOST --insecure-skip-tls-verify=true
kubectl config set-context riski-context --cluster=riski-cluster --user=riski-user
kubectl config use-context riski-context

kubectl get deployments -n $NAMESPACE -o jsonpath='
{range .items[*]}
- deployment: {.metadata.name}
  replicas: {.status.replicas}
  pods:
  {range .spec.template.spec.containers[*]}
    - name: {.name}
      requests:
        cpu: {.resources.requests.cpu}
        memory: {.resources.requests.memory}
      limits:
        cpu: {.resources.limits.cpu}
        memory: {.resources.limits.memory}
  {end}
{end}'


kubectl get pods -n $NAMESPACE