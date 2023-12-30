import 'dart:convert';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:inventory_management_application/pages/home.dart';
import 'package:nfc_manager/nfc_manager.dart';
import 'package:http/http.dart' as http;

class RetrievePage extends StatelessWidget {
  const RetrievePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: appBar(context),
      body: const Center(
        child: Column(
          children: [
            Divider(height: 50),
            Text("Retrieve clothes", style: TextStyle(color: Colors.white, fontSize: 36),),
            RetrieveClothes()
          ],
        ),
      ),
    );
  }

  AppBar appBar(BuildContext context) {
    return AppBar(
      title: Row(
        children: [
          GestureDetector(
            onTap: () {
              Navigator.push(context, MaterialPageRoute(builder: (context) => const HomePage()));
            },
            child: const Text("< Go back", style: TextStyle(fontSize: 30)),
          ),
          const Expanded(child: Divider(color: Colors.white, indent: 10, endIndent: 10, thickness: 2,)),
        ]
        ),
    backgroundColor: const Color(0xFF262735),
    elevation: 0.0,
    automaticallyImplyLeading: false,
    );
  }
}

class RetrieveClothes extends StatefulWidget {
  const RetrieveClothes({
    super.key,
  });

  @override
  State<RetrieveClothes> createState() => _RetrieveClothesState();
}

class _RetrieveClothesState extends State<RetrieveClothes> {

  String rackNumber = '';
  String name = '';

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance?.addPostFrameCallback((timeStamp) {
      initNFC();
    });
  }

  void initNFC() async {
    bool isAvailable = await NfcManager.instance.isAvailable();
    if (isAvailable) {
      NfcManager.instance.startSession(
        onDiscovered: (NfcTag tag) async {
          readBracelet(tag);
          NfcManager.instance.stopSession();
        },
      );
    } else {
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

  void readBracelet(NfcTag tag) async {
    var ndef = Ndef.from(tag);
    final data = await ndef?.read();
    Uint8List uint8list = data!.records[0].payload;
    String dataString = String.fromCharCodes(uint8list);
    dataString = dataString.replaceFirst(RegExp(r'^[^\{]*'), '');
    Map<String, dynamic> jsonMap = json.decode(dataString);
    setState(() {
      rackNumber = jsonMap['id'].toString();
      name = jsonMap['occupied_by'];
    });
  }

  @override
  Widget build(BuildContext context) {
    if (rackNumber == '' || name == '') {
      return whileSearchingContainer();
    }
    return whileFoundContainer(context);
  }

  Container whileFoundContainer(BuildContext context) {
    return Container(
    margin: const EdgeInsets.only(top: 25, right: 20, bottom: 20, left: 20),
    padding: const EdgeInsets.all(30),
    decoration: const BoxDecoration(
      borderRadius: BorderRadius.all(Radius.circular(10)),
      color: Color(0xFF323447),
    ),
    child: Column(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        const Text(
          "Bracelet found",
          style: TextStyle(color: Colors.white, fontSize: 32),
          textAlign: TextAlign.center,
        ),
        const Divider(height: 40),
        Row(
          children: [
            Expanded(
              child: DecoratedBox(
                decoration: const BoxDecoration(color: Color(0xFF4E516C), borderRadius: BorderRadius.all(Radius.circular(10))),
                child: Padding(
                  padding: const EdgeInsets.only(top: 10, right: 10, bottom: 10, left: 10),
                  child: Text(
                    "Rack: $rackNumber",
                    style: const TextStyle(color: Colors.white, fontSize: 24,),
                    textAlign: TextAlign.center,
                  ),
                ),
              ),
            ),
          ],
        ),
        const Divider(height: 20),
        Row(
          children: [
            Expanded(
              child: DecoratedBox(
                decoration: const BoxDecoration(color: Color(0xFF4E516C), borderRadius: BorderRadius.all(Radius.circular(10))),
                child: Padding(
                  padding: const EdgeInsets.only(top: 10, right: 10, bottom: 10, left: 10),
                  child: Text(
                    "Name: $name",
                    style: const TextStyle(color: Colors.white, fontSize: 24,),
                    textAlign: TextAlign.center,
                  ),
                ),
              ),
            ),
          ],
        ),
        const Divider(height: 40),
        ElevatedButton(
          style: ElevatedButton.styleFrom(
            padding: const EdgeInsets.all(10),
            foregroundColor: Colors.white,
            backgroundColor: const Color(0xFF832828),
          ),
          onPressed: () async {
            final response = await http.post(
              Uri.http(
                '34.38.115.7:8393', 'api/racks/$rackNumber/free'
              )
            );
            if (response.statusCode == 200) {
              // ignore: use_build_context_synchronously
              Navigator.push(context, MaterialPageRoute(builder: (context) => const HomePage()));
            }
          },
          child: const Text('Retrieve clothes'),
        ),
      ],
    ),
  );
  }


  Container whileSearchingContainer() {
    return Container(
    margin: const EdgeInsets.only(top: 25, right: 20, bottom: 20, left: 20),
    padding: const EdgeInsets.all(30),
    decoration: const BoxDecoration(
      borderRadius: BorderRadius.all(Radius.circular(10)),
      color: Color(0xFF323447)
    ),
    child: const Column(
      crossAxisAlignment: CrossAxisAlignment.center,
      children: [
        Text("please hold  your bracelet to the back of the device and make sure nfc is enabled", style: TextStyle(color: Colors.white), textAlign: TextAlign.center,),
        Divider(height: 40),
        Text("Searching for bracelet...", style: TextStyle(color: Colors.white), textAlign: TextAlign.center,)
      ],
    )
  );
  }
}