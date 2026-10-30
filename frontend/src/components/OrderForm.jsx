import { useState } from 'react'
import { placeOrder } from '../api'

// Dropdown value for "type your own product", used to test the unknown-product rejection
const OTHER = '__other__'

function OrderForm({ products, onPlaced }) {
  const [choice, setChoice] = useState('')
  const [customId, setCustomId] = useState('')
  const [quantity, setQuantity] = useState(1)
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState(null)

  // Until the user picks something, default to the first product once the list has loaded
  const selected = choice || products[0]?.id || ''
  const productId = selected === OTHER ? customId.trim() : selected

  async function handleSubmit(event) {
    // Stop the browser's default form behavior (a full page reload)
    event.preventDefault()
    setSubmitting(true)
    setError(null)
    try {
      await placeOrder(productId, Number(quantity))
      onPlaced()
    } catch (err) {
      setError(err.message)
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <form className="order-form" onSubmit={handleSubmit}>
      <label>
        Product
        <select value={selected} onChange={(e) => setChoice(e.target.value)}>
          {products.map((p) => (
            <option key={p.id} value={p.id}>
              {p.id} ({p.stock === 0 ? 'out of stock' : `${p.stock} left`})
            </option>
          ))}
          <option value={OTHER}>Other (type a product)…</option>
        </select>
      </label>
      {selected === OTHER && (
        <label>
          Product code
          <input
            autoFocus
            placeholder="e.g. ZZZ"
            value={customId}
            onChange={(e) => setCustomId(e.target.value.toUpperCase())}
          />
        </label>
      )}
      <label>
        Quantity
        <input
          type="number"
          min="1"
          value={quantity}
          onChange={(e) => setQuantity(e.target.value)}
        />
      </label>
      <button type="submit" disabled={submitting || !productId}>
        {submitting ? 'Placing…' : 'Place order'}
      </button>
      {error && <p className="form-error">{error}</p>}
      <p className="hint">Try ordering more than is in stock, or pick Other for a product that doesn't exist.</p>
    </form>
  )
}

export default OrderForm
