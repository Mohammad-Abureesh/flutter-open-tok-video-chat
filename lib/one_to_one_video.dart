part of flutter_open_tok;

class VideoChannel {
  static const platformMethodChannel =
      MethodChannel('com.vonage.one_to_one_video');
}

class OneToOneVideo extends StatelessWidget {
  final SessionConfig config;
  final PreferredSizeWidget? appBar;

  const OneToOneVideo({Key? key, required this.config, this.appBar})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: appBar,
      floatingActionButton: FloatingActionButton(
          onPressed: () {
            //open chat view
            showDialog(
                context: context,
                builder: (_) {
                  final size = MediaQuery.of(context).size;
                  return AlertDialog(
                      content: SizedBox(
                          width: size.width - 150.0,
                          height: size.height - 200.0,
                          child: const SignalWidget(title: 'Chat')));
                });
          },
          child: const Icon(Icons.chat, size: 25.0)),
      body: CallWidget(title: 'Video call', config: config),
      // Column(children: const [
      //   Expanded(child: CallWidget(title: 'One to One Video')),
      //   Expanded(child: SignalWidget(title: 'CHAT'))
      // ])
    );
  }
}

class CallWidget extends StatefulWidget {
  final SessionConfig config;
  const CallWidget(
      {Key key = const Key("any_key"),
      required this.title,
      required this.config})
      : super(key: key);
  final String title;

  @override
  _CallWidgetState createState() => _CallWidgetState();
}

class _CallWidgetState extends State<CallWidget> {
  SdkState _sdkState = SdkState.loggedOut;
  bool _publishAudio = true;
  bool _publishVideo = true;

  static const platformMethodChannel = VideoChannel.platformMethodChannel;

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
      'apiKey': widget.config.apiKey,
      'sessionId': widget.config.sessionId,
      'token': widget.config.token
    };

    try {
      await platformMethodChannel.invokeMethod(
          'initSession', widget.config.encode());
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print(e);
      }
    }
  }

  Future<void> requestPermissions() async {
    await [Permission.microphone, Permission.camera].request();
  }

  Future<void> _swapCamera() async {
    try {
      await platformMethodChannel.invokeMethod('swapCamera');
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print(e);
      }
    }
  }

  Future<void> _toggleAudio() async {
    _publishAudio = !_publishAudio;

    dynamic params = {'publishAudio': _publishAudio};

    try {
      await platformMethodChannel.invokeMethod('toggleAudio', params);
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print(e);
      }
    }
  }

  Future<void> _toggleVideo() async {
    _publishVideo = !_publishVideo;
    _updateView();

    dynamic params = {'publishVideo': _publishVideo};

    try {
      await platformMethodChannel.invokeMethod('toggleVideo', params);
    } on PlatformException catch (e) {
      if (kDebugMode) {
        print(e);
      }
    }
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
            child: const PlatFormSpecificView(key: Key("any_key")),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              ElevatedButton(
                onPressed: () {
                  _swapCamera();
                },
                child: const Text("Swap " "Camera"),
              ),
              ElevatedButton(
                onPressed: () {
                  _toggleAudio();
                },
                style: ButtonStyle(
                  backgroundColor: MaterialStateProperty.resolveWith<Color>(
                    (Set<MaterialState> states) {
                      if (!_publishAudio) return Colors.grey;
                      return Colors.lightBlue;
                    },
                  ),
                ),
                child: const Text("Toggle Audio"),
              ),
              ElevatedButton(
                onPressed: () {
                  _toggleVideo();
                },
                style: ButtonStyle(
                  backgroundColor: MaterialStateProperty.resolveWith<Color>(
                    (Set<MaterialState> states) {
                      if (!_publishVideo) return Colors.grey;
                      return Colors.lightBlue;
                    },
                  ),
                ),
                child: const Text("Toggle Video"),
              ),
            ],
          ),
        ],
      );
    } else {
      return const Center(child: Text("ERROR"));
    }
  }
}

class PlatFormSpecificView extends StatelessWidget {
  static const StandardMessageCodec _decoder = StandardMessageCodec();
  const PlatFormSpecificView({
    required Key key,
  }) : super(key: key);
  @override
  Widget build(BuildContext context) {
    final Map<String, String> args = {"someInit": "initData"};
    if (defaultTargetPlatform == TargetPlatform.android) {
      return AndroidView(
          viewType: 'opentok-video-container',
          creationParams: args,
          creationParamsCodec: _decoder);
    }
    return UiKitView(
        viewType: 'opentok-video-container',
        creationParams: args,
        creationParamsCodec: _decoder);
  }
}
