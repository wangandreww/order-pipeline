function StockPanel({ products }) {
  // Bars are relative to the best-stocked product, with a floor so small numbers don't fill the bar
  const max = Math.max(10, ...products.map((p) => p.stock))

  return (
    <ul className="stock">
      {products.map((product) => (
        <li key={product.id}>
          <div className="stock-label">
            <span className="mono">{product.id}</span>
            <span className={product.stock === 0 ? 'out' : ''}>
              {product.stock === 0 ? 'Out of stock' : `${product.stock} left`}
            </span>
          </div>
          <div className="bar">
            <div
              className={`bar-fill${product.stock <= 2 ? ' low' : ''}`}
              style={{ width: `${(product.stock / max) * 100}%` }}
            />
          </div>
        </li>
      ))}
    </ul>
  )
}

export default StockPanel
