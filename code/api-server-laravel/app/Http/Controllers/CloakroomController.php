<?php

namespace App\Http\Controllers;

use App\Modules\Services\CloakroomService;

use Illuminate\Http\Request;
class CloakroomController extends Controller
{
    private CloakroomService $service;

    public function __construct(CloakroomService $service)
    {
        $this->service = $service;
    }

    public function get()
    {
        return $this->service->get();
    }

    public function toggleStatus(string $status)
    {
        return $this->service->toggleStatus($status);
    }

    public function editRacks(Request $request)
    {
        $rackAmount = $request->input('max_racks');
        return $this->service->editRacks($rackAmount);
    }
}
