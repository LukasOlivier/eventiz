<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Cloakroom extends Model
{
    use HasFactory;

    protected $fillable = [
        'open',
    ];

    protected $hidden = [
        'created_at',
        'updated_at',
        'id'
    ];

    public function racks()
    {
        return $this->hasMany(Rack::class);
    }

    protected $appends = [
        'racks',
    ];

    public function getRacksAttribute()
    {
        return $this->racks()->get();
    }

}
