<?php

namespace App\Http\Controllers;

use App\Modules\Services\EmergencyTypeService;
use Illuminate\Routing\Route;

class EmergencyTypeController extends Controller
{
    private EmergencyTypeService $service;

    public function __construct(EmergencyTypeService $service)
    {
        $this->service = $service;
    }

    public function get(Route $route)
    {
        $type = $route->parameter('type');
        return $this->service->get($type);
    }
}
