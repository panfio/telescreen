#!/bin/bash
#set -x; 
#This script copy files from android phone into archive folder

ARCHIVE_FOLDER=$1

MTP=$(ls /run/user/1000/gvfs)
PHONE_STORAGE="/run/user/1000/gvfs/$MTP/Internal shared storage"

copy() {
#Params: from | to | mask
find "$PHONE_STORAGE$1" -type f -name $3 | xargs -i cp -v -u {} $ARCHIVE_FOLDER$2
}

#Not require root
copy "/DCIM/Camera" "/media/photo/" "IMG_*.jpg"
copy "/DCIM/Camera" "/media/video/" "VID_*.mp4" 
copy "/EasyVoiceRecorder" "/media/voicenote/" "*note.m4a" 
copy "/Pictures/Screenshots" "/media/screenshot/" "Screenshot*.png" 
copy "/Day One" "/app/dayone/" "Export-Journal.zip" 
copy "/Timesheet" "/app/timesheet/" "TimesheetBackup*.xml"

# #root
copy "/archive/app/soundcloud" "/app/soundcloud/" "collection.db" 
copy "/archive/app/soundcloud" "/app/soundcloud/" "SoundCloud"
copy "/archive/app/spotify" "/app/spotify/" "*.bnk"
copy "/archive/app/sms" "/app/sms/" "bugle_db"
copy "/archive/app/call" "/app/call/" "calllog.db"
copy "/archive/app/wellbeing" "/app/wellbeing/" "app_usage"

echo "===== Done ====="

