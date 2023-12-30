import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vite';

export default defineConfig({
	plugins: [sveltekit()],
	define: {
		'import.meta.env.VITE_PUBLIC_LARAVEL_URL': process.env.PUBLIC_LARAVEL_URL,
		'import.meta.env.VITE_PUBLIC_STRAPI_URL': process.env.PUBLIC_STRAPI_URL,
		'import.meta.env.VITE_PUBLIC_API_TOKEN': process.env.PUBLIC_API_TOKEN,
		'import.meta.env.VITE_PUBLIC_ECHO_WSHOST': process.env.PUBLIC_ECHO_WSHOST,
		'import.meta.env.VITE_PUBLIC_ECHO_WSPORT': process.env.PUBLIC_ECHO_WSPORT,
		'import.meta.env.VITE_PUBLIC_ECHO_CLUSTER': process.env.PUBLIC_ECHO_CLUSTER,
		'import.meta.env.VITE_PUBLIC_ECHO_KEY': process.env.PUBLIC_ECHO_KEY,
		'import.meta.env.VITE_PUBLIC_ECHO_FORCE_TLS': process.env.PUBLIC_ECHO_FORCE_TLS,
		'import.meta.env.VITE_PUBLIC_ECHO_DISABLE_STATS': process.env.PUBLIC_ECHO_DISABLE_STATS,
		'import.meta.env.VITE_PUBLIC_ECHO_BROADCAST_DRIVER': process.env.PUBLIC_ECHO_BROADCAST_DRIVER,
	
	  },
});
