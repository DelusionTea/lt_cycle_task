file=$1
targetDirectory=$2
bucket="bennu-edz"
resource="/${bucket}/${file}"
contentType="application/x-compressed-tar"
dateValue=`date -u +"%a, %d %b %Y %H:%M:%S GMT"`
stringToSign="PUT\n\n${contentType}\n${dateValue}\n${targetDirectory}${file}"
s3Key=I8LYVR4237G0NSJ9ISL2
s3Secret=EnXSSm2XcQ9pOOeSBajtr5YHmKWcFfkNbJa0GG5w
signature=`echo -en ${stringToSign} | openssl sha1 -hmac ${s3Secret} -binary | base64`
curl -f -X PUT -T "${file}" \
  -H "Host: ${bucket}.delta.sbrf.ru" \
  -H "Date: ${dateValue}" \
  -H "Content-Type: ${contentType}" \
  -H "Authorization: AWS ${s3Key}:${signature}" \
  https://${bucket}.delta.sbrf.ru${targetDirectory}${file}
