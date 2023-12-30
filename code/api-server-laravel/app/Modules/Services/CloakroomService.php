<?php

namespace App\Modules\Services;


use App\Events\RackUpdate;
use App\Models\Cloakroom;

class CloakroomService
{
    protected Cloakroom $model;

    public function __construct(Cloakroom $model)
    {
        $this->model = $model;
    }

    public function get()
    {
        $cloakroom = $this->model->first();
        $cloakroom->occupied_racks = $cloakroom->racks()->where('occupied_by', '!=', null)->count();
        $cloakroom->max_racks = $cloakroom->racks()->count();
        return collect($cloakroom);
    }

    public function toggleStatus($status)
    {
        $cloakroom = $this->model->first();
        if ($status === 'close') {
            $cloakroom->open = 0;
        } else {
            $cloakroom->open = 1;
        }
        $cloakroom->save();
        broadcast(new RackUpdate());
        return $this->get();
    }

    public function editRacks($rackAmount){
        $cloakroom = $this->model->first();

        if ($cloakroom->open === 1) {
            return response()->json([
                'message' => 'Cloakroom is open, please close it first',
            ], 400);
        }
        $cloakroom->racks()->delete();
        $cloakroom->racks()->truncate();

        for ($i = 0; $i < $rackAmount; $i++) {
            $cloakroom->racks()->create([
                'occupied_by' => null,
            ]);
        }
        $cloakroom->save();
        broadcast(new RackUpdate());
        return $this->get();
    }

}
