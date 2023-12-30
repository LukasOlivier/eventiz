import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:inventory_management_application/pages/home.dart';
import 'package:http/http.dart' as http;
import 'package:nfc_manager/nfc_manager.dart';

class StorePage extends StatelessWidget {
  const StorePage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      appBar: appBar(context),
      body: const Center(
        child: Column(children: [
          Divider(height: 50),
          Text("Store Clothes", style: TextStyle(fontSize: 36, color: Colors.white),),
          StoreClothes()
        ]),
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

class StoreClothes extends StatefulWidget {
  const StoreClothes({
    super.key,
  });

  @override
  State<StoreClothes> createState() => _StoreClothesState();
}

class _StoreClothesState extends State<StoreClothes> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    final textController = TextEditingController();
    return Container(
      margin: const EdgeInsets.only(top: 25, right: 20, bottom: 20, left: 20),
      padding: const EdgeInsets.all(30),
      decoration: const BoxDecoration(
        borderRadius: BorderRadius.all(Radius.circular(10)),
        color: Color(0xFF323447),
      ),
      child: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text("Write the bracelet to store the clothes", style: TextStyle(color: Colors.white, fontSize: 22), textAlign: TextAlign.center,),
            const Divider(height: 40,),
            TextField(
              controller: textController,
              style: const TextStyle(
                color: Colors.white
              ),
              decoration: const InputDecoration(
                fillColor: Color(0xFFFFFFFF),
                labelText: "Enter visitor name",
                labelStyle: TextStyle(
                  color: Colors.white
                ),
                enabledBorder: UnderlineInputBorder(
                  borderSide: BorderSide(
                    width: 1, color: Colors.white
                  )
                )
              ),
            ),
            const Divider(height: 20,),
            Center(
              child: ElevatedButton(
                      style: ElevatedButton.styleFrom(
                        padding: const EdgeInsets.all(10),
                        foregroundColor: Colors.white, backgroundColor: const Color(0xFF832828), // foreground
                      ),
                      onPressed: () { waitForBracelet(textController.text); },
                      child: const Text('Write bracelet'),
              )
            ),
          ]),
      ),
    );
  }

  void waitForBracelet(String name) async {
    try {
      final freeRack = await getFreeRack(name);
      bool isAvailable = await NfcManager.instance.isAvailable();
      if (isAvailable) {
        NfcManager.instance.startSession(
        onDiscovered: (NfcTag tag) async {
            writeBracelet(tag, name, freeRack);
            NfcManager.instance.stopSession();
            Navigator.push(context, MaterialPageRoute(builder: (context) => const HomePage()));
          }
        );
      } else {
        showNFCDialog();
      }
    } catch(e) {
      throw Exception("failed to get a free rack");
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

  void writeBracelet(NfcTag tag, String name, int freeRack) {
    try {
      var ndef = Ndef.from(tag);

      Map<String, dynamic> dataMap = {
        'id': freeRack,
        'occupied_by': name,
      };

      String jsonString = jsonEncode(dataMap);

      final record = NdefRecord.createText(jsonString);
      final message = NdefMessage([record]);
      ndef?.write(message);
    } catch(e) {
      writeBracelet(tag, name, freeRack);
    }
  }

  Future<int> getFreeRack(String name) async {
    final response = await http.post(Uri.http(
      '34.38.115.7:8393', 'api/racks/occupy'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8'
      },
      body: jsonEncode(<String, String>{
        'occupied_by': name
      })
    );
    if (response.statusCode == 200) {
      final responseBody = jsonDecode(response.body);
      final id = responseBody['id'] as int;
      return id;
    } else {
      throw Exception("Failed to get a free rack");
    }
  }
}