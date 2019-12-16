#!/bin/bash
#set -x; 
#This script copy files from android phone into archive folder


ARCHIVE_FOLDER='/home/apoh/archive'

MTP=$(ls /run/user/1000/gvfs)
PHONE_STORAGE="/run/user/1000/gvfs/$MTP/Internal shared storage"

#Not require root
find "$PHONE_STORAGE/DCIM/Camera" -type f -name "IMG_*.jpg" | xargs -i cp -v -u {} $ARCHIVE_FOLDER/media/photo/
find "$PHONE_STORAGE/DCIM/Camera" -type f -name "VID_*.mp4" | xargs -i cp -v -u {} $ARCHIVE_FOLDER/media/video/
find "$PHONE_STORAGE/EasyVoiceRecorder" -type f -name "*note.m4a" | xargs -i cp -v -u {} $ARCHIVE_FOLDER/media/voicenote/
find "$PHONE_STORAGE/Pictures/Screenshots" -type f -name "Screenshot*.png" | xargs -i cp -v -u {} $ARCHIVE_FOLDER/media/screenshot/
find "$PHONE_STORAGE'/Day One'" -type f -name "Export-Journal.zip" | xargs -i cp -v -u {} $ARCHIVE_FOLDER/app/dayone/
find "$PHONE_STORAGE/Timesheet" -type f -name "TimesheetBackup*.xml" | xargs -i cp -v -u {} $ARCHIVE_FOLDER/app/timesheet/

# #root
find "$PHONE_STORAGE/archive/app/soundcloud" -type f -name "*.db" | xargs -i cp -v -u {} $ARCHIVE_FOLDER/app/soundcloud/
find "$PHONE_STORAGE/archive/app/soundcloud" -type f -name "SoundCloud" | xargs -i cp -v -u {} $ARCHIVE_FOLDER/app/soundcloud/
find "$PHONE_STORAGE/archive/app/spotify" -type f -name "*.bnk" | xargs -i cp -v -u {} $ARCHIVE_FOLDER/app/spotify/
find "$PHONE_STORAGE/archive/app/sms" -type f -name "bugle_db" | xargs -i cp -v -u {} $ARCHIVE_FOLDER/app/sms/
find "$PHONE_STORAGE/archive/app/call" -type f -name "calllog.db" | xargs -i cp -v -u {} $ARCHIVE_FOLDER/app/call/

echo "===== Done ====="

