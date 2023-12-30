<?php

namespace App\Events;

use App\Models\Cloakroom;
use Illuminate\Broadcasting\Channel;
use Illuminate\Broadcasting\InteractsWithSockets;
use Illuminate\Contracts\Broadcasting\ShouldBroadcast;
use Illuminate\Foundation\Events\Dispatchable;
use Illuminate\Queue\SerializesModels;

class RackUpdate implements ShouldBroadcast
{
    use Dispatchable, InteractsWithSockets, SerializesModels;

    /**
     * Create a new event instance.
     */


    public function broadcastWith(): array
    {
        $cloakroom = Cloakroom::first();
        $cloakroom->occupied_racks = $cloakroom->racks()->where('occupied_by', '!=', null)->count();
        $cloakroom->max_racks = $cloakroom->racks()->count();
        return [collect($cloakroom)];
    }

    /**
     * Get the channels the event should broadcast on.
     *
     * @return array<int, Channel>
     */
    public function broadcastOn(): array
    {
        return [
            new Channel('racks'),
        ];
    }
}
