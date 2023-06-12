## TubeCC

This is the newer version of Tube CC made using Angular 16 & Spring boot.

Add subtitle to Youtube, Vimeo, DailyMotion and many more videos (See Supported Videos). Paste the video link in the text box above and search the subtitle. If the subtitle is found then click on the link to add it to the video or upload your srt/vtt file.

### Build & Run
```
docker build -t tubecc .
docker run -d -p80:8080 tubecc
```
Open http://localhost

The application can be accessed using below URL which is deployed in the cloud

http://bit.ly/tubecc-n
