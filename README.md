### Never Ending Background Service Experiment

#### Desired results:
Scenario 1. Restart service when app is stopped by Android or via Settings > Running Apps > Stop
Scenario 2. Restart service when app is stopped via swiping in recent apps


##### Works in:
1. Samsung S8 - Nougat
2. Huawei Honor 4x - KitKat
3. Alcatel Flash Plus 2 - Marshmallow

##### Not yet working in (bug in scenario 2):
1. Google Nexus - Oreo
2. Asus Zenfone 2 - Lollipop 21
3. StarMobile Jump HD - Lollipop 20



##### Bug: 
Service not being restarted when app is killed via swiping in recent apps (real device testing not emulator)

##### Note: 
Emulator not accurate; services restart is working in Oreo, Lollipop but not on real device as tested

##### Source:
Based on https://fabcirablog.weebly.com/blog/creating-a-never-ending-background-service-in-android
