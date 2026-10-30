// Every call goes through Vite's proxy (see vite.config.js), so paths start with /api

async function request(path, options) {
  const res = await fetch(path, options)
  if (!res.ok) {
    throw new Error(`${res.status} ${res.statusText}`)
  }
  return res.json()
}

export const getOrders = () => request('/api/orders')

export const getProducts = () => request('/api/products')

export async function placeOrder(productId, quantity) {
  try {
    return await request('/api/orders', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ productId, quantity }),
    })
  } catch (err) {
    // The backend's 400 doesn't say which field failed yet, so give a useful hint instead
    if (err.message.startsWith('400')) {
      throw new Error('Enter a product and a quantity of at least 1.')
    }
    throw err
  }
}
