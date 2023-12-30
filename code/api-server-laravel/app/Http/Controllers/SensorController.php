<?php

namespace App\Http\Controllers;

use App\Modules\Services\SensorService;
use Illuminate\Http\Request;

class SensorController extends Controller
{
    private SensorService $service;

    public function __construct(SensorService $service)
    {
        $this->service = $service;
    }

    public function get(Request $request)
    {
        $type = $request->query('type');
        return $this->service->get($type);
    }

    public function patch(String $type, Request $request)
    {

        $data = $request->all();

        return $this->service->patch($type, $data['value']);
    }


}
