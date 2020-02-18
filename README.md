# Telescreen Server

Telescreen helps aggregate and monitor your actions in one place and visually present them on a timeline. Now you will know how and where your time was spent.
<img src="https://raw.githubusercontent.com/panfio/demohttpd/master/website/telescreen.jpg" alt="telescreen in action" />
<!--You can start service and store data on a local computer, on any Kubernetes cluster or on a remote server like Heroku and use Amazon S3 as a data store.
-->
If you find a bug or have any suggestions and feature request feel free to open an issue.
**Important! App is under active development and not ready for production!**

## Quickstart

**Kubernetes guide: [charts/README.md](https://github.com/panfio/telescreen/blob/master/charts/README.md)**

**Docker-Compose guide:**

**Requirements:** locally installed Docker and docker-compose or https://labs.play-with-docker.com/

Run this in the terminal.

```sh
git clone https://github.com/panfio/telescreen
cd telescreen
export ARCHIVE_FOLDER=`pwd`/archive
#get example images
for i in {1..9}; do  wget -O ./archive/app/media/photo/IMG_2020010${i}_1${i}5411.jpg https://picsum.photos/200; done

docker-compose up
```

Add a new media files inside `./archive/app/media` folders or upload to `media` bucket using minio at [localhost:9000](http://localhost:9000). Open browser at [localhost:8888](http://localhost:8888) (default credentials is `user : passpass`), start processing and find the images between 1 January 2020 and January 10, 2020.

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
./scripts/copy.sh /archive
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
- **Spotify** Get token [Here](https://developer.spotify.com/console/get-track/) and paste it as parameter before start processing

### For Android users with root

You can manualy copying databases and related files or use cron jobs for automation. Download a terminal emulator [Termux](https://play.google.com/store/apps/details?id=com.termux).
Copy archive folder into external storage and `./scripts/upload.py` into `/sdcard/upload.py`. Replace minio host inside `upload.py`

```sh
pkg install python
pip install minio
echo '
#!/bin/sh
su -c cp /data/data/com.android.providers.contacts/databases/calllog.db /sdcard/archive/app/call/calllog.db
su -c cp /data/data/com.google.android.apps.messaging/databases/bugle_db /sdcard/archive/app/sms/bugle_db
su -c cp /data/data/com.soundcloud.android/databases/collection.db /sdcard/archive/app/soundcloud/collection.db
su -c cp /data/data/com.soundcloud.android/databases/SoundCloud /sdcard/archive/app/soundcloud/SoundCloud
su -c cp /data/data/com.whatsapp/databases/msgstore.db /sdcard/archive/app/whatsapp/msgstore.db
su -c cp /data/data/com.google.android.apps.wellbeing/databases/app_usage /sdcard/archive/app/wellbeing/app_usage
su -c cp /data/data/com.xiaomi.hm.health/databases/`su -c ls /data/data/com.xiaomi.hm.health/databases | grep origin_db | awk '!/journal/'` /sdcard/archive/app/mifit/db/origin_db
/data/data/com.termux/files/usr/bin/python /sdcard/upload.py' > sync.sh
chmod +x sync.sh
./sync.sh
```

## Integrations

- Activity log using [KalleHallden/AutoTimer](https://github.com/KalleHallden/AutoTimer)
- Timesheet app: [timesheet.io](https://timesheet.io/en/)
- Easy Voice Recorder app: [Play Store](https://play.google.com/store/apps/details?id=com.coffeebeanventures.easyvoicerecorder)
- SoundCloud: [SoundCloud](https://soundcloud.com/)
- YouTube history: [Takeout](https://takeout.google.com/)
- Mi Fit: [Play Store](https://play.google.com/store/apps/details?id=com.xiaomi.hm.health)
- Telegram

## HighLights

- [Dec 22, 2019](https://github.com/panfio/telescreen/blob/b575ab3cb885d06b7b36d81a919f366f512978e0/telescreen/src/main/java/ru/panfio/telescreen/service/ProcessService.java)

- [Feb 03, 2020](https://github.com/panfio/telescreen/blob/master/handler/src/main/java/ru/panfio/telescreen/handler/service/AutoTimerService.java)

## How it works

##### Data microservice

Reads Kafka topics and saves items into MongoDB. Uses Spring Data REST for managing the data.

Java 8 | Spring Boot / Data / Kafka | MongoDB

##### Handler microservice

The service handles export, media and historical files stored in the Minio storage and sends the processed items to Kafka.

Java 11 | Spring Boot / Data JDBC / Kafka | Minio | SQLite

##### Gateway microservice

Redirects requests between services and serves static frontend content.

Java 8 | Spring: Boot / Zuul

TypeScript | React | Redux

Frontend components:

- [material-ui](https://material-ui.com/)
- [material-ui-pickers](https://material-ui-pickers.dev/)
- [react-calendar-timeline](https://github.com/namespace-ee/react-calendar-timeline)
- [reactdatepicker](https://reactdatepicker.com/)

## To start developing Telescreen

Ensure you have a working Java, NPM, Docker environment.
Uncomment the required services in `docker-compose.yml` in the DEV section. Then run:

```sh
git clone https://github.com/panfio/telescreen
cd telescreen
./scripts/build.sh
export ARCHIVE_FOLDER=`pwd`\archive
docker-compose up
cd frontend/
REACT_APP_API_URL=http://localhost:8080 npm start

#for sonarqube
cd telescreen
./mvnw clean verify sonar:sonar
```
