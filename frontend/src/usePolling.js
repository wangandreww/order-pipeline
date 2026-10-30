import { useCallback, useEffect, useState } from 'react'

// Calls load() now and then every intervalMs, keeping the latest result.
// load must be a stable function (defined outside the component), or the timer restarts on every render.
export function usePolling(load, intervalMs) {
  const [data, setData] = useState(null)
  const [error, setError] = useState(null)

  const refresh = useCallback(
    () =>
      load()
        .then((result) => {
          setData(result)
          setError(null)
        })
        .catch((err) => setError(err.message)),
    [load],
  )

  useEffect(() => {
    refresh()
    const timer = setInterval(refresh, intervalMs)
    // Cleanup: stop the timer when the component goes away
    return () => clearInterval(timer)
  }, [refresh, intervalMs])

  return { data, error, refresh }
}
