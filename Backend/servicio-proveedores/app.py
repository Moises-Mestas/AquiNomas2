from flask import Flask, request, jsonify
import proveedores
import pybreaker
import os
import random
from tenacity import retry, stop_after_attempt, wait_fixed

app = Flask(__name__)
PORT = int(os.environ.get("PORT", random.randint(5001, 5999)))

proveedores.registrar_en_eureka(PORT)

breaker = pybreaker.CircuitBreaker(fail_max=3, reset_timeout=10)


@app.route('/health')
def health():
    return "OK", 200




if __name__ == "__main__":
    app.run(port=PORT, debug=False)
