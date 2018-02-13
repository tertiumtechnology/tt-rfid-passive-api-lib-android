# tt-rfid-passive-api-lib-android

The rfidpassiveapilib module depends on the [txrxlib](https://github.com/tertiumtechnology/tt-txrx-lib-android) base
module.

The current configuration require that all modules folder should reside in the same directory, as in the example below:

```bash
AndroidProjectsFolder/
¦
+-- tt-rfid-passive-api-lib-android/
  ¦
  +-- rfidpassiveapilib/
  +-- settings.gradle
  ...
+-- tt-txrx-lib-android/
  ¦
  +-- txrxlib/
  ...
...
```

If needed, you can change the settings.gradle file inside the module folder, according to your project structure.