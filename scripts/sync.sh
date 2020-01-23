#!/bin/sh
su -c cp /data/data/com.android.providers.contacts/databases/calllog.db /sdcard/archive/app/call/calllog.db
su -c cp /data/data/com.google.android.apps.messaging/databases/bugle_db /sdcard/archive/app/sms/bugle_db
su -c cp /data/data/com.soundcloud.android/databases/collection.db /sdcard/archive/app/soundcloud/collection.db
su -c cp /data/data/com.soundcloud.android/databases/SoundCloud /sdcard/archive/app/soundcloud/SoundCloud
su -c cp /data/data/com.whatsapp/databases/msgstore.db /sdcard/archive/app/whatsapp/msgstore.db
su -c cp /data/data/com.google.android.apps.wellbeing/databases/app_usage /sdcard/archive/app/wellbeing/app_usage
su -c cp /data/data/com.xiaomi.hm.health/databases/`su -c ls /data/data/com.xiaomi.hm.health/databases | grep origin_db | awk '!/journal/'` /sdcard/archive/app/mifit/db/origin_db
/data/data/com.termux/files/usr/bin/python /sdcard/upload.py