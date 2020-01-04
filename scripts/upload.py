from minio import Minio
import os
import time
from minio.error import (ResponseError)

host = "localhost:9000"
app_bucket = "app"
media_bucket = "media"
access_key='user'
secret_key='passpass'
internal_storage="/sdcard"

minioClient = Minio(host, access_key, secret_key, secure=False)

def put(media_bucket, fileInBucket, file_path):
    try:
        object_stat = minioClient.stat_object(media_bucket, fileInBucket)
        if time.mktime(object_stat.last_modified) < time.mktime(time.localtime(os.stat(file_path).st_mtime)):
            print("copying... " + fileInBucket)
            minioClient.fput_object(media_bucket, fileInBucket, file_path)
    except ResponseError as err:
        print(err)
    except Exception as e:
        print("copying... " + fileInBucket)
        minioClient.fput_object(media_bucket, fileInBucket, file_path)

def sync_folder(folder, mask, bucket, bucket_folder):
    for file in os.listdir(folder):
        if file.endswith(mask):
            put(bucket, bucket_folder + file, os.path.join(folder, file))
    
folder = internal_storage + "/archive"
for (path, dirs, files) in os.walk(folder):
    for file in files:
        fileInapp_bucket = path.replace(folder + '/' + app_bucket + '/', '') + '/' + file
        put(app_bucket, fileInapp_bucket, os.path.join(path, file))

sync_folder(internal_storage + "/DCIM/Camera", ".jpg", app_bucket, 'media/photo/')
sync_folder(internal_storage + "/DCIM/Camera", ".mp4", app_bucket, 'media/video/')
sync_folder(internal_storage + "/Pictures/Screenshots", ".png", app_bucket, 'media/screenshot/')
sync_folder(internal_storage + "/EasyVoiceRecorder", ".m4a", app_bucket, 'media/voicenote/')
sync_folder(internal_storage + "/Timesheet", ".xml", app_bucket, 'timesheet/')