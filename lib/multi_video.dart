import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/services.dart';
import 'package:opentok_flutter_samples/src/config/sdk_states.dart';
import 'package:permission_handler/permission_handler.dart';

import 'flutter_open_tok_lib.dart';

class MultiVideo extends StatelessWidget {
  const MultiVideo({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text("Multi Party Video"),
        ),
        body: const CallWidget(title: 'Multi Party Video'));
  }
}

class CallWidget extends StatefulWidget {
  const CallWidget({Key key = const Key("any_key"), required this.title})
      : super(key: key);
  final String title;

  @override
  _CallWidgetState createState() => _CallWidgetState();
}

class _CallWidgetState extends State<CallWidget> {
  SdkState _sdkState = SdkState.loggedOut;

  static const platformMethodChannel = MethodChannel('com.vonage.multi_video');

  _CallWidgetState() {
    platformMethodChannel.setMethodCallHandler(methodCallHandler);

    _initSession();
  }

  Future<dynamic> methodCallHandler(MethodCall methodCall) async {
    switch (methodCall.method) {
      case 'updateState':
        {
          setState(() {
            var arguments = 'SdkState.${methodCall.arguments}';
            _sdkState = SdkState.values.firstWhere((v) {
              return v.toString() == arguments;
            });
          });
        }
        break;
      default:
        throw MissingPluginException('notImplemented');
    }
  }

  Future<void> _initSession() async {
    await requestPermissions();

    dynamic params = {
      'apiKey': OpenTokConfig.apiKey,
      'sessionId': OpenTokConfig.sessionID,
      'token': OpenTokConfig.token
    };

    try {
      await platformMethodChannel.invokeMethod('initSession', params);
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print(e);
      }
    }
  }

  Future<void> requestPermissions() async {
    await [Permission.microphone, Permission.camera].request();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[const SizedBox(), _updateView()],
      ),
    );
  }

  Widget _updateView() {
    if (_sdkState == SdkState.loggedOut) {
      return ElevatedButton(
          onPressed: () {
            _initSession();
          },
          child: const Text("Init session"));
    } else if (_sdkState == SdkState.wait) {
      return const Center(
        child: CircularProgressIndicator(),
      );
    } else if (_sdkState == SdkState.loggedIn) {
      return Column(
        children: [
          SizedBox(
            height: MediaQuery.of(context).size.height / 2,
            child: PlatformViewLink(
              viewType: 'opentok-multi-video-container',
              surfaceFactory: (context, controller) {
                return AndroidViewSurface(
                  controller: controller as AndroidViewController,
                  gestureRecognizers: const <
                      Factory<OneSequenceGestureRecognizer>>{},
                  hitTestBehavior: PlatformViewHitTestBehavior.opaque,
                );
              },
              onCreatePlatformView: (PlatformViewCreationParams params) {
                return PlatformViewsService.initSurfaceAndroidView(
                  id: params.id,
                  viewType: 'opentok-multi-video-container',
                  layoutDirection: TextDirection.ltr,
                  creationParams: {},
                  creationParamsCodec: const StandardMessageCodec(),
                  onFocus: () {
                    params.onFocusChanged(true);
                  },
                )
                  ..addOnPlatformViewCreatedListener(
                      params.onPlatformViewCreated)
                  ..create();
              },
            ),
          ),
        ],
      );
    } else {
      return const Center(child: Text("ERROR"));
    }
  }
}
