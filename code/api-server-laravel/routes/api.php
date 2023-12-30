<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\CloakroomController;
use App\Http\Controllers\EmergencyController;
use App\Http\Controllers\EmergencyTypeController;
use App\Http\Controllers\RackController;
use App\Http\Controllers\SensorController;
use App\Http\Controllers\SensorStatisticsController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/

Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();

});

Route::middleware('auth:api')->group(function () {
    Route::post("/cloakroom/{status}", [CloakroomController::class, "toggleStatus"]);
    Route::post("/cloakroom/racks/edit", [CloakroomController::class, "editRacks"]);
});

// make a POST route to trigger an alarm, just websockets is fine
Route::post("/alarm", function () {
    broadcast(new \App\Events\AlarmUpdate());
});

Route::patch("/sensors/{type}", [SensorController::class, "patch"])
    ->middleware('EnsureTokenIsValid');

Route::post("/racks/occupy", [RackController::class, "occupy"])
    ->middleware('EnsureTokenIsValid');

Route::post("/racks/{rack}/free", [RackController::class, "free"])
    ->middleware('EnsureTokenIsValid');

Route::post('/login', [AuthController::class, "login"])
    ->withoutMiddleware('EnsureTokenIsValid');

Route::post("/emergency", [EmergencyController::class, "emergency"])
    ->withoutMiddleware('EnsureTokenIsValid');

Route::get("/emergencies", [EmergencyController::class, "get"])
    ->withoutMiddleware('EnsureTokenIsValid');

Route::get("/emergency-types", [EmergencyTypeController::class, "get"])
    ->withoutMiddleware('EnsureTokenIsValid');

Route::get("/sensors", [SensorController::class, "get"])
    ->withoutMiddleware('EnsureTokenIsValid');

Route::get("/sensors/statistics", [SensorStatisticsController::class, "getStatistics"])
    ->withoutMiddleware('EnsureTokenIsValid');

Route::get("/racks", [RackController::class, "get"])
    ->withoutMiddleware('EnsureTokenIsValid');

Route::get("/cloakroom", [CloakroomController::class, "get"])
    ->withoutMiddleware('EnsureTokenIsValid');


