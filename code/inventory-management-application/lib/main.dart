import 'package:flutter/material.dart';
import 'package:inventory_management_application/pages/home.dart';

void main() {

  runApp( const MyApp() );

}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: "EventIZ",
      theme: ThemeData(
        scaffoldBackgroundColor: const Color(0xFF262735),
        primaryColor: const Color(0xFFFFFFFF),
        fontFamily: 'Staatliches'
        ),
      home: const HomePage(),
      debugShowCheckedModeBanner: false,
    );
  }
}