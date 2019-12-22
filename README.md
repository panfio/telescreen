# Telescreen Server

Telescreen helps aggregate and monitor your actions in one place and visually present them on a timeline. Now you will know how and where your time was spent
<img src="https://raw.githubusercontent.com/panfio/demohttpd/master/website/telescreen.jpg" alt="telescreen in action" />
<!--You can start service and store data on a local computer, on any Kubernetes cluster or on a remote server like Heroku and use Amazon S3 as a data store.
-->
If you find a bug or have any suggestions and feature request feel free to open an issue.
**Important! App is under active development and not ready for production!
Only you control what data should be collected and where it should be stored**

## Quickstart

**Requirements:**

- Docker and docker-compose

Run this in the terminal.

```sh
git clone https://github.com/panfio/telescreen
cd telescreen
export ARCHIVE_FOLDER=`pwd`/archive
docker-compose up
```

Add a new media files inside `archive/media` folders or upload to `media` bucket using minio [localhost:9000](http://localhost:9000). 
Open browser at [localhost:8888](http://localhost:8888) (default credentials `user : passpass`)

**Warning! Docker containers can be accessible from the network even if firewall is enabled!** Check this question for details: [stackoverflow.com/questions/49549834](https://stackoverflow.com/questions/49549834/ufw-firewall-is-not-working-on-ubuntu-in-digitalocean/49563279#49563279).

**Interaction**
*Be careful when choosing a time interval. In 2 days, up to 2 thousand events can happen.*
To interact and navigate within the timeline there are the following options:

```sh
shift + mousewheel = move timeline left/right
alt + mousewheel = zoom in/out
ctrl + mousewheel = zoom in/out 10× faster
meta + mousewheel = zoom in/out 3x faster (win or cmd + mousewheel)
```

## How to configure apps for using with Telescreen

- **Media files** Try using a script that copies files from your Android device. Due to differences in hardware and operating systems, please manually set the paths `ARCHIVE_FOLDER` and `PHONE_STORAGE` inside. The script also copies the backup files.

```bash
./scripts/copy.sh
```

Dates can be parsed correctly from the following file names. Other files saves with the creation date.

```sh
IMG_20200101_010203.jpg         Screenshot_20200101-010203.png
VID_20200101_010203.mp4         Screenshot from 2020-01-01 01-02-03.png
2019-10-05-22-00-55-note.m4a
```

- **AutoTimer**
Make sure you have working python, pip, virtualenv.
Clone AutoTimer into your archive folder. Set the archive folder location and run script in terminal

```sh
ARCHIVE_FOLDER=`pwd`/archive
git clone https://github.com/KalleHallden/AutoTimer $ARCHIVE_FOLDER/app/autotimer
./scripts/autotimer.sh $ARCHIVE_FOLDER/app/autotimer
```

- **Timesheet** Configure daily backup to Timesheet folder on the external storage
- **Day One** Configure daily backup in the app settings
- **Spotify** You need to get token [Here](https://developer.spotify.com/console/get-track/) and paste it as parameter before start processing

### For Android users with root

You can manualy copying databases and related files or use cron jobs for automation. Download a terminal emulator [Termux](https://play.google.com/store/apps/details?id=com.termux).
Copy archive folder into external storage.

```sh
crontab -e -u root #edit crontab
crond -b #run service
ps -aux #check that crond  is running
```

Paste this into the cron editor. Make sure that the path `/sdcard` correctly points to `EXTERNAL_STORAGE` and replace “XXX” with your Spotify ID.

```
0 * * * * su -c cp /data/data/com.spotify.music/files/settings/Users/XXXXXXXXXXXXXXXXXXXXXXXXXX/recently_played.bnk /sdcard/archive/app/spotify/recently_played-`date "+%Y%m%d-%H%M%S"`.bnk
0 0 * * * su -c cp -u /data/data/com.android.providers.contacts/databases/calllog.db /sdcard/archive/app/call/calllog.db
0 0 * * * su -c cp -u /data/data/com.google.android.apps.messaging/databases/bugle_db /sdcard/archive/app/sms/bugle_db
0 0 * * * su -c cp -u /data/data/com.soundcloud.android/databases/collection.db /sdcard/archive/app/soundcloud/collection.db
0 0 * * * su -c cp -u /data/data/com.soundcloud.android/databases/SoundCloud /sdcard/archive/app/soundcloud/SoundCloud
0 0 * * * su -c cp -u /data/data/com.whatsapp/databases/msgstore.db /sdcard/archive/app/whatsapp/msgstore.db
0 0 * * * su -c cp -u /data/data/com.google.android.apps.wellbeing/databases/app_usage /sdcard/archive/app/wellbeing/app_usage
```

## Integrations

- Activity log using [KalleHallden/AutoTimer](https://github.com/KalleHallden/AutoTimer)
- Timesheet app: [timesheet.io](https://timesheet.io/en/)
- Easy Voice Recorder app: [Play Store](https://play.google.com/store/apps/details?id=com.coffeebeanventures.easyvoicerecorder)
- Spotify
- SoundCloud
- YouTube history [Takeout](https://takeout.google.com/)
<!-- - Day One app: [dayoneapp.com](https://dayoneapp.com/) -->
## Used technologies

TypeScript | React | [react-calendar-timeline](https://github.com/namespace-ee/react-calendar-timeline)

Java 1.8 | SpringBoot 2.1 | Swagger | [Spotify Web API](https://github.com/thelinmichael/spotify-web-api-java) | [protobuf](https://github.com/protocolbuffers/protobuf)

[Minio](https://min.io/) | H2

## To start developing Telescreen

Ensure you have a working Java, NPM, Docker environment.
Then run:

```sh
git clone https://github.com/panfio/telescreen
cd telescreen
./scripts/build.sh
export ARCHIVE_FOLDER=`pwd`\archive
docker-compose up
cd frontend/
REACT_APP_API_URL=http://localhost:8080 npm start
```
