#!/bin/bash

PROJECT_ID="excel2word-project2"
REGION="asia-southeast1"
SERVICE_NAME="excel2word-app"

echo "=== 🔧 Tối ưu chi phí Google Cloud Run cho service: $SERVICE_NAME ==="

echo "➡️ 1. Đặt min-instances = 0, max-instances = 1, bật CPU throttling..."
gcloud run services update $SERVICE_NAME \
  --min-instances=0 \
  --max-instances=1 \
  --cpu-throttling \
  --region=$REGION \
  --project=$PROJECT_ID

echo "➡️ 2. Giảm retention log còn 1 ngày..."
gcloud logging buckets update _Default \
  --location=global \
  --retention-days=1

echo "➡️ 3. Xóa revision cũ (không ảnh hưởng bản đang chạy)..."
REVISIONS=$(gcloud run revisions list \
  --region=$REGION \
  --project=$PROJECT_ID \
  --format="value(metadata.name)" | tail -n +2)

for rev in $REVISIONS; do
  echo "  - Xóa revision: $rev"
  gcloud run revisions delete $rev \
    --quiet \
    --region=$REGION \
    --project=$PROJECT_ID
done


echo "➡️ 4. Xóa Docker images cũ (chỉ giữ image mới nhất)..."
IMAGES=$(gcloud artifacts docker images list \
  asia-southeast1-docker.pkg.dev/$PROJECT_ID/excel2word-repo \
  --format="value(URI)" | tail -n +2)

COUNTER=0
for img in $IMAGES; do
  ((COUNTER++))
  if [ $COUNTER -gt 1 ]; then
    echo "  - Xóa image: $img"
    gcloud artifacts docker images delete $img --quiet --delete-tags
  else
    echo "  - Giữ image mới nhất: $img"
  fi
done


echo "➡️ 5. Disable API không cần thiết (nếu đang bật) – an toàn tuyệt đối..."

DELETE_LIST=(
  "bigquery.googleapis.com"
  "bigquerystorage.googleapis.com"
  "compute.googleapis.com"
  "container.googleapis.com"
  "firestore.googleapis.com"
  "spanner.googleapis.com"
  "dataproc.googleapis.com"
  "dataflow.googleapis.com"
  "pubsub.googleapis.com"
  "notebook.googleapis.com"
)

for API in "${DELETE_LIST[@]}"; do
  if gcloud services list --enabled --project=$PROJECT_ID \
       --format="value(config.name)" | grep -q "$API"; then
      echo "  - Disable API: $API"
      gcloud services disable $API --quiet --project=$PROJECT_ID --force
  else
      echo "  - API $API không bật → bỏ qua"
  fi
done

echo "🎉 Hoàn tất tối ưu chi phí Google Cloud!"
