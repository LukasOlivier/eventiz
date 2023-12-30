<?php

namespace App\Http\Controllers;

use App\Modules\Services\EmergencyService;
use Illuminate\Http\Request;

class EmergencyController extends Controller
{
    private EmergencyService $service;

    public function __construct(EmergencyService $service)
    {
        $this->service = $service;
    }

    public function get(Request $request)
    {
        $type = $request->query('type');
        return $this->service->get($type);
    }

    public function emergency(Request $request)
    {
        $emergency = $request->all();
        return $this->service->post($emergency);
    }
}
