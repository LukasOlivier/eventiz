<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Rack extends Model
{
    use HasFactory;

    protected $fillable = [
        'occupied_by',
    ];

    protected $hidden = [
        'created_at',
        'updated_at',
        'cloakroom_id'
    ];
}

