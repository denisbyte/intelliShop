export async function blobToJson<T>(blob: Blob): Promise<T> {
    const text = await blob.text();
    return JSON.parse(text) as T;
  }
  