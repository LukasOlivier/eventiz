import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:inventory_management_application/pages/retrieve.dart';
import 'package:inventory_management_application/pages/store.dart';
import 'package:http/http.dart' as http;
import 'package:nfc_manager/nfc_manager.dart';
import 'package:web_socket_channel/io.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  State<HomePage> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {

  final channel = IOWebSocketChannel.connect("ws://eventiz-ws.lukasolivier.be/app/websocket");

  String racks = "Loading...";

  @override
  void initState() {
    super.initState();
    _subscribeToChannel();
    getFreeRacks();
    testNFCAvailable();
  }

  void testNFCAvailable() async {
    bool isAvailable = await NfcManager.instance.isAvailable();
    if (!isAvailable) {
      showNFCDialog();
    }
  }

  void showNFCDialog() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('NFC is not available'),
          content: Text('Please enable NFC in your device settings.'),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.pop(context); // Close the dialog
              },
              child: Text('OK'),
            ),
          ],
        );
      },
    );
  }

  void _subscribeToChannel() {
    final subscribeEvent = {
      "event":"pusher:subscribe",
      "data": {
        "auth":"",
        "channel":"racks"
      }
    };
    channel.sink.add(jsonEncode(subscribeEvent));
    channel.stream.listen((message) {});
  }

  void getFreeRacks() async {
    try {
      var response = await http.get(Uri.http('eventiz-laravel.lukasolivier.be', '/api/cloakroom'));
      if (response.statusCode == 200) {
        var jsonData = jsonDecode(response.body);
        setState(() {
          racks = "${jsonData['occupied_racks']} / ${jsonData['max_racks']}";
        });
      }
    } catch(e) {
      throw Exception('Error: $e');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: appBar(),
      body: Center(
        child: Column(
          children: [
            freeRackWidget(racks),
            storeClothes(context),
            retrieveClothes(context)
          ]),
      ),
    );
  }

  GestureDetector retrieveClothes(BuildContext context) {
    return GestureDetector(
      onTap: () {
        Navigator.push(context, MaterialPageRoute(builder: (context) => const RetrievePage()));
      },
      child: Container(
            margin: const EdgeInsets.only(top: 10),
            height: 80,
            width: 300,
            decoration: const BoxDecoration(borderRadius: BorderRadius.all(Radius.circular(10)), color: Color(0xFF832828)),
            child: const Center(child: Text("Retrieve clothes", style: TextStyle(color: Colors.white, fontSize: 40),)),
          ),
    );
  }

  GestureDetector storeClothes(BuildContext context) {
    return GestureDetector(
      onTap: () {
        Navigator.push(context, MaterialPageRoute(builder: (context) => const StorePage()));
      },
      child: Container(
              margin: const EdgeInsets.only(top: 200),
              height: 80,
              width: 300,
              decoration: const BoxDecoration(borderRadius: BorderRadius.all(Radius.circular(10)), color: Color(0xFF323447)),
              child: const Center(child: Text("Store clothes", style: TextStyle(color: Colors.white, fontSize: 40),)),
            ),
    );
  }

Container freeRackWidget(String racks) {
  return Container(
    margin: const EdgeInsets.only(top: 50, left: 40, right: 40, bottom: 10),
    padding: const EdgeInsets.all(15),
    height: 150,
    width: 300,
    decoration: const BoxDecoration(
      borderRadius: BorderRadius.all(Radius.circular(10)),
      color: Color(0xFF323447),
    ),
    child: Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          "free racks",
          style: TextStyle(color: Colors.white, fontSize: 20),
        ),
        Expanded(
          child: Align(
            child: Text(
              racks,
              style: const TextStyle(color: Colors.white, fontSize: 50),
            ),
          ),
        ),
      ],
    ),
  );
}

  AppBar appBar() {
    return AppBar(
      title:
        const Row(
          children: [
            Expanded(child: Divider(color: Colors.white, indent: 10, endIndent: 10, thickness: 2,)),
            Text("EventIZ", style: TextStyle(fontSize: 40)),
            Expanded(child: Divider(color: Colors.white, indent: 10, endIndent: 10, thickness: 2,),)
          ],
        ),
      centerTitle: true,
      backgroundColor: const Color(0xFF262735),
      elevation: 0.0,
      automaticallyImplyLeading: false,
    );
  }
}