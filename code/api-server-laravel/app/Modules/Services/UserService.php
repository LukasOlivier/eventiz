<?php

namespace App\Modules\Services;

use App\Models\User;
use Couchbase\QueryException;
use Illuminate\Auth\AuthenticationException;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Http\JsonResponse;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;
use Nette\Schema\ValidationException;
use PHPUnit\Exception;

class UserService
{

    protected User $model;
    private array $loginRules = [
        'email' => 'required|string|email',
        'password' => 'required',
    ];


    public function __construct(User $model)
    {
        $this->model = $model;
    }

    /**
     * @throws AuthenticationException
     */
    public function login($credentials): ?string
    {
        $validator = Validator::make($credentials, $this->loginRules);
        if ($validator->fails()) throw new ValidationException($validator->errors());
        $token = auth()->attempt($credentials);
        if ($token == null) throw new AuthenticationException("Wrong credentials");
        return $token;
    }

}
