module.exports = ({ env }) => ({
  auth: {
    secret: env('ADMIN_JWT_SECRET', 'secret'),
  },
  apiToken: {
    salt: env('API_TOKEN_SALT', 'salt'),
  },
  transfer: {
    token: {
      salt: env('TRANSFER_TOKEN_SALT', 'secret'),
    },
  },
});
