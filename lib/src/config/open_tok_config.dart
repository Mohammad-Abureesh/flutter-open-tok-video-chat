part of flutter_open_tok;

class OpenTokConfig {
  static const apiKey = "46297612";
  static const sessionID =
      "2_MX40NjI5NzYxMn5-MTY4NDgzMTk4ODcyN342dGNxVEdJUXE1Y0VnV0Q3aUVubW9aZVZ-fn4";
  static const token =
      "T1==cGFydG5lcl9pZD00NjI5NzYxMiZzaWc9MmJmMmRiMGIzYTJkNjNkNTliM2Q2OGJlMjIxYjM5NzIzYzk3MDZkNTpzZXNzaW9uX2lkPTJfTVg0ME5qSTVOell4TW41LU1UWTRORGd6TVRrNE9EY3lOMzQyZEdOeFZFZEpVWEUxWTBWblYwUTNhVVZ1Ylc5YVpWWi1mbjQmY3JlYXRlX3RpbWU9MTY4NDgzMjAyMyZub25jZT0tNTM1NTA1NzQ1JnJvbGU9cHVibGlzaGVyJmV4cGlyZV90aW1lPTE2ODQ5MTg0MjMmY29ubmVjdGlvbl9kYXRhPW5hbWUlM0RDdXN0b21lcg==";
}

class SessionConfig {
  SessionConfig({
    required this.apiKey,
    required this.sessionId,
    required this.token,
  });

  String apiKey;
  String sessionId;
  String token;

  Object encode() {
    final Map<Object?, Object?> map = <Object?, Object?>{};
    map['apiKey'] = apiKey;
    map['sessionId'] = sessionId;
    map['token'] = token;
    return map;
  }

  static SessionConfig decode(Object message) {
    final Map<Object?, Object?> map = message as Map<Object?, Object?>;
    return SessionConfig(
      apiKey: map['apiKey']! as String,
      sessionId: map['sessionId']! as String,
      token: map['token']! as String,
    );
  }
}
