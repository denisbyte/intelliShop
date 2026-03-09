import argparse
import json
from pathlib import Path

import numpy as np
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity


DEFAULT_WEIGHTS = {
    "ADD_TO_CART": 3.0,
    "CLICK": 1.0,
    "VIEW": 0.5,
    "REMOVE_FROM_CART": -2.0,
}


def load_json(path: str):
    p = Path(path)
    if not p.exists():
        raise FileNotFoundError(f"Fichier introuvable: {path}")
    with p.open("r", encoding="utf-8") as f:
        return json.load(f)


def build_user_item_matrix(interactions: list, weights: dict) -> pd.DataFrame:
    # garde uniquement les events utiles + userId non null
    rows = []
    for e in interactions:
        user_id = e.get("userId")
        produit_id = e.get("produitId")
        typ = e.get("type")
        if user_id is None or produit_id is None or typ is None:
            continue
        w = weights.get(typ, 0.0)
        if w == 0.0:
            continue
        rows.append((int(user_id), int(produit_id), float(w)))

    if not rows:
        return pd.DataFrame()

    df = pd.DataFrame(rows, columns=["userId", "produitId", "w"])
    # agrégation (si plusieurs events identiques → somme)
    mat = df.pivot_table(
        index="userId", columns="produitId", values="w", aggfunc="sum", fill_value=0.0
    )
    return mat


def compute_item_similarity(user_item: pd.DataFrame) -> pd.DataFrame:
    # item vectors = colonnes (produits)
    item_matrix = user_item.values.T  # shape: (n_items, n_users)
    sim = cosine_similarity(item_matrix)
    sim_df = pd.DataFrame(sim, index=user_item.columns, columns=user_item.columns)
    return sim_df


def recommend_for_user(
    user_id: int,
    products_df: pd.DataFrame,
    user_item: pd.DataFrame,
    item_sim: pd.DataFrame,
    top_k: int = 8,
):
    # Popularité (fallback)
    popularity = user_item.sum(axis=0).sort_values(ascending=False)

    if user_item.empty or user_id not in user_item.index:
        # cold start: top popular
        rec_ids = popularity.head(top_k).index.tolist()
        rec = products_df[products_df["id"].isin(rec_ids)].copy()
        rec["score"] = rec["id"].map(popularity).fillna(0.0)
        rec = rec.sort_values("score", ascending=False)
        return rec

    user_vec = user_item.loc[user_id]
    seen = user_vec[user_vec > 0].index.tolist()

    if len(seen) == 0:
        rec_ids = popularity.head(top_k).index.tolist()
        rec = products_df[products_df["id"].isin(rec_ids)].copy()
        rec["score"] = rec["id"].map(popularity).fillna(0.0)
        rec = rec.sort_values("score", ascending=False)
        return rec

    # score(item) = somme(sim(item, seen_i) * interaction_weight(seen_i))
    scores = pd.Series(0.0, index=item_sim.index)
    for pid in seen:
        scores = scores.add(item_sim[pid] * float(user_vec[pid]), fill_value=0.0)

    # ne pas recommander ce que l’utilisateur a déjà
    scores.loc[seen] = -np.inf

    # topK
    top = scores.sort_values(ascending=False).head(top_k)

    rec = products_df[products_df["id"].isin(top.index)].copy()
    rec["score"] = rec["id"].map(top).fillna(0.0)
    rec = rec.sort_values("score", ascending=False)
    return rec


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--products", required=True, help="produits.json")
    ap.add_argument("--interactions", required=True, help="interactions.json")
    ap.add_argument("--user", required=True, type=int, help="userId")
    ap.add_argument("--top", default=8, type=int, help="Top K recommendations")
    args = ap.parse_args()

    products = load_json(args.products)
    interactions = load_json(args.interactions)

    products_df = pd.DataFrame(products)
    if "id" not in products_df.columns:
        raise ValueError("produits.json doit contenir un champ 'id' pour chaque produit")

    weights = DEFAULT_WEIGHTS

    user_item = build_user_item_matrix(interactions, weights)
    if user_item.empty:
        raise ValueError("Aucune interaction exploitable. Vérifie interactions.json")

    item_sim = compute_item_similarity(user_item)

    rec_df = recommend_for_user(
        user_id=args.user,
        products_df=products_df,
        user_item=user_item,
        item_sim=item_sim,
        top_k=args.top,
    )

    # sortie JSON
    out = rec_df[["id", "nom", "prix", "imageUrl", "score"]].to_dict(orient="records")
    print(json.dumps(out, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()

