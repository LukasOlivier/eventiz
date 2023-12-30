<?php

namespace App\Modules\Services;


use App\Events\RackUpdate;
use App\Models\Cloakroom;
use App\Models\Rack;

class RackService
{
    protected Rack $model;

    public function __construct(Rack $model)
    {
        $this->model = $model;
    }

    public function get($occupied)
    {
        if ($occupied == null) {
            return $this->model->all();
        }
        else if ($occupied == 'true') {
            return $this->model->where('occupied_by', '!=', null)->get();
        }
        return $this->model->where('occupied_by', null)->get();
    }

    public function free($rackNumber)
    {
        $rack = $this->model->where('id', $rackNumber)->first();
        $rack->occupied_by = null;
        $rack->save();
        broadcast(new RackUpdate());
        return $rack;
    }

    public function occupy($value){
        if ($this->checkIfCloakroomClosed()){
            return "Cloakroom is closed";
        }

        $rack = $this->model->where('occupied_by', null)->first();
        if ($rack == null) {
            return "No available rack found.";
        }
        $rack->occupied_by = $value['occupied_by'];
        $rack->save();
        broadcast(new RackUpdate());
        return $rack;
    }

    private function checkIfCloakroomClosed(): bool
    {
        $cloakroom = Cloakroom::first();
        return $cloakroom->open == 0;
    }


}
