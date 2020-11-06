public class CacheOverrideMiddleware { // insert this using
    public static CacheOverrideMiddleware add(AsyncHttpClient client) {
        CacheOverrideMiddleware ret = new CacheOverrideMiddleware();
        // add this first so it gets called before everything else
        client.getMiddleware().add(ret);
        return ret;
    }

    @Override
    public void onHeadersReceived(OnHeadersReceivedDataOnRequestSentData data) {
        super.onHeadersReceived(data);

        // do more checking here, since uri may not necessarily be http or have a host, etc.
        String cache = cacheHeaders.get(data.request.getUri().getHost());
        if (!TextUtils.isEmpty(cache))
            data.response.headers().set("Cache-Control", cache);
    }

    Hashtable<String, String> cacheHeaders = new Hashtable<String, String>();

    /**
     * Override cache-control directives
     * @param host
     * @param cacheControl a Cache-Control value, like "max-age=300" to cache for 5 minutes
     */
    public void setCacheControlForHost(String host, String cacheControl) {
        cacheHeaders.put(host, cacheControl);
    }
    
    }
