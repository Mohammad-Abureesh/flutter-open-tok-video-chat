import 'package:flutter/material.dart';
import 'package:opentok_flutter_samples/screen_sharing.dart';
import 'package:opentok_flutter_samples/signaling.dart';

import 'archiving.dart';
import 'flutter_open_tok_lib.dart';
import 'multi_video.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: HomeScreen(),
    );
  }
}

class HomeScreen extends StatelessWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text("Flutter Video SDK Samples"),
        ),
        body: Center(
            child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[const SizedBox(), _updateView(context)],
        )));
  }
}

Widget _updateView(BuildContext context) {
  return Column(children: [
    Row(mainAxisAlignment: MainAxisAlignment.spaceEvenly, children: [
      ElevatedButton(
          onPressed: () {
            final config = SessionConfig(
                apiKey: '46297612',
                sessionId:
                    '2_MX40NjI5NzYxMn5-MTY4NDgzMzQwMDk0MX5lanEwV3pIMmN0ZHc5YVZBL1o4RjZFb2N-fn4',
                token:
                    'T1==cGFydG5lcl9pZD00NjI5NzYxMiZzaWc9NWRkZWFhYzExODIxODZmZTBjOWZkYWYyOWNkMzAxNmU3NjFmOTY5YjpzZXNzaW9uX2lkPTJfTVg0ME5qSTVOell4TW41LU1UWTRORGd6TXpRd01EazBNWDVsYW5Fd1YzcElNbU4wWkhjNVlWWkJMMW80UmpaRmIyTi1mbjQmY3JlYXRlX3RpbWU9MTY4NDgzMzQwOCZub25jZT0tMzQ1Mzk5NzEyJnJvbGU9cHVibGlzaGVyJmV4cGlyZV90aW1lPTE2ODQ5MTk4MDgmY29ubmVjdGlvbl9kYXRhPW5hbWUlM0RDdXN0b21lcg==');

            Navigator.push(
                context,
                MaterialPageRoute(
                    builder: (context) => OneToOneVideo(config: config)));
          },
          child: const Text("One to One Video Call")),
    ]),
    Row(mainAxisAlignment: MainAxisAlignment.spaceEvenly, children: [
      ElevatedButton(
          onPressed: () {
            Navigator.push(context,
                MaterialPageRoute(builder: (context) => const MultiVideo()));
          },
          child: const Text("Multi Party Video Call")),
    ]),
    Row(mainAxisAlignment: MainAxisAlignment.spaceEvenly, children: [
      ElevatedButton(
          onPressed: () {
            Navigator.push(context,
                MaterialPageRoute(builder: (context) => const Signaling()));
          },
          child: const Text("Signalling")),
    ]),
    Row(mainAxisAlignment: MainAxisAlignment.spaceEvenly, children: [
      ElevatedButton(
          onPressed: () {
            Navigator.push(context,
                MaterialPageRoute(builder: (context) => const Archiving()));
          },
          child: const Text("Archiving")),
    ]),
    Row(mainAxisAlignment: MainAxisAlignment.spaceEvenly, children: [
      ElevatedButton(
          onPressed: () {
            Navigator.push(context,
                MaterialPageRoute(builder: (context) => const ScreenSharing()));
          },
          child: const Text("View Sharing")),
    ]),
  ]);
}
