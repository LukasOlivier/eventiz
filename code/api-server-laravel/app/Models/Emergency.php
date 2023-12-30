<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Emergency extends Model
{
    use HasFactory;

    protected $fillable = [
        'type_id',
        'location',
        'created_at',
    ];

    protected $hidden = [
        'updated_at',
        'type_id',
    ];

    public function type()
    {
        return $this->belongsTo(EmergencyType::class);
    }

    public function getTypeAttribute()
    {
        return $this->type()->value('type');
    }
    protected $appends = ['type'];
}

